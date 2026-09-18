import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { components } from "@/types/api";
import GoalDefinitionActionMenu from "./GoalDefinitionActionMenu";

type GoalDefinitionsProps = {
  challengeId: number;
  goalDefinitions: components["schemas"]["GoalDefinitionResponse"][]; // get this from components schema
};

export default function GoalDefinitions({
  challengeId,
  goalDefinitions,
}: GoalDefinitionsProps) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Goal</TableHead>
          <TableHead className="text-right">Action</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {goalDefinitions.map((goal) => (
          <TableRow key={goal.id}>
            <TableCell>{goal.name}</TableCell>
            <TableCell className="flex justify-end">
              <GoalDefinitionActionMenu
                challengeId={challengeId}
                goalDefinitionId={goal.id}
              />
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
