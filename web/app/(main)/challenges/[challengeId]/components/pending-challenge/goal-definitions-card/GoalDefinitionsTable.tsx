import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Props } from "./GoalDefinitionsCard";
import { DeleteGoalDefinition } from "./DeleteGoalDefinition";

export function GoalDefinitionsTable({
  challengeId,
  goalDefinitions,
  viewOnly,
}: Props) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Goal</TableHead>
          {!viewOnly && <TableHead className="text-right">Action</TableHead>}
        </TableRow>
      </TableHeader>
      <TableBody>
        {goalDefinitions.map((goal) => (
          <TableRow key={goal.id}>
            <TableCell>{goal.name}</TableCell>
            {!viewOnly && (
              <TableCell className="flex justify-end">
                <DeleteGoalDefinition
                  challengeId={challengeId}
                  goalDefinitionId={goal.id}
                />
              </TableCell>
            )}
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
