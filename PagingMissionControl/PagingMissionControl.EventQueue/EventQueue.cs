using PagingMissionControl.EventQueue.Factories;
using PagingMissionControl.EventQueue.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;

namespace PagingMissionControl.EventQueue
{
    /// <summary>Event queue for a pub/sub model. Events are sent from one part of the application to another. Each event is tagged with a specific unique identifier.</summary>
    /// <remarks>Egads! I am using service-locator pattern here! Definitely something to be avoid. However, for the centralized Event Queue object that passes messages throughout the application, there is really little alternative.
    /// <para />
    /// We strike a compromise between this and sound design principles through the use of an interface to mark the data type of the Instance property.</remarks>
    public class EventQueue : IEventQueue
    {
        /// <summary>Reference to the instance of an object that is used for thread synchronization.</summary>
        private static readonly object SyncRoot = new object();

        /// <summary>Empty, static constructor to prohibit direct allocation of this class.</summary>
        static EventQueue() { }

        /// <summary>Empty, protected constructor to prohibit direct allocation of this class.</summary>
        protected EventQueue() { }

        /// <summary>Gets a reference to the one and only instance of <see cref="T:PagingMissionControl.EventQueue.EventQueue" />.</summary>
        public static IEventQueue Instance { get; } = new EventQueue();

        /// <summary>
        /// Gets a collection of references to instances of objects that implement the
        /// <see
        ///     cref="T:PagingMissionControl.EventQueue.Interfaces.IEventQueueItem" />
        /// interface.
        /// </summary>
        private IList<IEventQueueItem> QueueItems { get; } =
            new List<IEventQueueItem>();

        /// <summary>Associates a handler method with an event having a specific unique identifier.
        /// <para />
        /// When the event having the identifier specified is later published to this event queue, all handler method(s) associated with the event are invoked.</summary>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <param name="handler">Reference to an instance of a handler method whose signature matches that of the <see cref="T:System.Delegate" /> delegate type or its children.</param>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="eventId" />, is passed a <see langword="null" /> value.</exception>
        public void MapEvent(Guid eventId, Delegate handler)
        {
            if (Guid.Empty == eventId)
                throw new ArgumentException(
                    "You may not pass the Zero GUID for the eventId parameter.",
                    nameof(eventId)
                );
            if (handler == null)
                throw new ArgumentNullException(nameof(handler));

            CreateEventMapping(eventId, handler);
        }

        /// <summary>Associates a handler method with an event having a specific unique identifier.
        /// <para />
        /// When the event having the identifier specified is later published to this event queue, all handler method(s) associated with the event are invoked.</summary>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <param name="handler">Reference to an instance of a handler method whose signature matches that of the <see cref="T:System.EventHandler" /> delegate type.</param>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="eventId" />, is passed a <see langword="null" /> value.</exception>
        public void MapEvent(Guid eventId, EventHandler handler)
        {
            if (Guid.Empty == eventId)
                throw new ArgumentException(
                    "You may not pass the Zero GUID for the eventId parameter.",
                    nameof(eventId)
                );
            if (handler == null)
                throw new ArgumentNullException(nameof(handler));

            CreateEventMapping(eventId, handler);
        }

        /// <summary>Associates a handler method with an event having a specific unique identifier.
        /// <para />
        /// When the event having the identifier specified is later published to this event queue, all handler method(s) associated with the event are invoked.</summary>
        /// <typeparam name="T">
        /// Type of data object, which must be of type
        /// <see
        ///     cref="T:System.EventArgs" />
        /// or one of its children, that carries data to be passed to the handler.
        /// </typeparam>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <param name="handler">Reference to an instance of a handler method whose signature matches that of the <see cref="T:System.EventHandler" /> delegate type.</param>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="eventId" />, is passed a <see langword="null" /> value.</exception>
        public void MapEvent<T>(Guid eventId, EventHandler<T> handler)
            where T : EventArgs
        {
            if (Guid.Empty == eventId)
                throw new ArgumentException(
                    "You may not pass the Zero GUID for the eventId parameter.",
                    nameof(eventId)
                );
            if (handler == null)
                throw new ArgumentNullException(nameof(handler));

            CreateEventMapping(eventId, handler);
        }

        /// <summary>Publishes the event with the unique event identifier that specified by the <paramref name="eventId" /> parameter.
        /// <para />
        /// This overload publishes events that do not pass any arguments to their handler.</summary>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <remarks>Use this overload to publish events that do not need any arguments supplied to their handlers.</remarks>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        public void PublishEvent(Guid eventId)
        {
            if (Guid.Empty == eventId)
                throw new ArgumentException(
                    "You may not pass the Zero GUID for the eventId parameter.",
                    nameof(eventId)
                );

            lock (SyncRoot)
            {
                if (QueueItems.All(item => item.EventId != eventId))
                    return;

                foreach (var handler in QueueItems
                                        .First(item => item.EventId == eventId)
                                        .HandlerList)
                {
                    handler?.DynamicInvoke();
                }
            }
        }

        /// <summary>Publishes the event with the event data</summary>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <param name="args">(Required.) One or more arguments to be passed to the invoked event handler.
        /// <para />
        /// The number, data type, and storage size of all arguments must be exactly the same as is specified in the event delegate's signature.</param>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        public void PublishEvent(Guid eventId, params object[] args)
        {
            if (Guid.Empty == eventId)
                throw new ArgumentException(
                    "You may not pass the Zero GUID for the eventId parameter.",
                    nameof(eventId)
                );
            if (!args.Any())    // call the overload that does not take an args array
            {
                PublishEvent(eventId);
                return;
            }

            lock (SyncRoot)
            {
                if (QueueItems.All(item => item.EventId != eventId))
                    return;

                foreach (var handler in QueueItems
                                        .First(item => item.EventId == eventId)
                                        .HandlerList)
                {
                    handler?.DynamicInvoke(args);
                }
            }
        }

        private void CreateEventMapping(Guid eventId, Delegate handler)
        {
            if (Guid.Empty == eventId)
                throw new ArgumentException(
                    "You may not pass the Zero GUID for the eventId parameter.",
                    nameof(eventId)
                );
            if (handler == null)
                throw new ArgumentNullException(nameof(handler));

            lock (SyncRoot)
            {
                if (QueueItems.Any(i => i.EventId == eventId))
                {
                    var itemForEventId =
                        QueueItems.First(i => i.EventId == eventId);
                    if (itemForEventId != null)
                        if (!itemForEventId.HandlerList.Contains(handler))
                            itemForEventId.HandlerList.Add(handler);
                }
                else
                {
                    QueueItems.Add(
                        MakeNewEventQueueItem.FromScratch()
                                             .ForEventId(eventId)
                                             .AndRemovalAction(
                                                 item => QueueItems.Remove(item)
                                             )
                                             .AndAddHandler(handler)
                    );
                }
            }
        }
    }
}