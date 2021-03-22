using PagingMissionControl.Constants;
using PagingMissionControl.Transformations;
using System;

namespace PagingMissionControl
{
    /// <summary>Provides the program's global functionality.</summary>
    public static class Program
    {
        /// <summary>Application entry point.</summary>
        public static void Main()
        {
            EventQueue.EventQueue.Instance.MapEvent(
                Events.EVENT_TRANSFORMATION_DONE, new Action<string>(OnDone)
            );

            TransformationEngine.Instance.DoConversion(
                AskUserFor.InputFilePath()
            );
        }

        /// <summary>Called when the transformation engine is done processing the data.</summary>
        /// <param name="result">(Required.) String parameter that receives the result of the data transformation operation.</param>
        private static void OnDone(string result)
        {
            if (string.IsNullOrWhiteSpace(result)) return;

            Display.Text(result);

            Wait.ForUserToPressAnyKey(); // to keep the console window from disappearing on Windows
        }
    }
}