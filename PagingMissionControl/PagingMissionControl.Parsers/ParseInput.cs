using PagingMissionControl.Interfaces;
using PagingMissionControl.Factories;
using System;
using System.Collections.Generic;
using System.Linq;

namespace PagingMissionControl.Parsers
{
    public static class ParseInput
    {
        public static IEnumerable<IInputRow> FromPipeDelimitedInputLines(
            IEnumerable<string> lines)
            => lines.Select(
                l => MakeNewInputRow.FromParts(
                    l.Split(new[] { '|' }, StringSplitOptions.RemoveEmptyEntries)
                )
            );
    }
}